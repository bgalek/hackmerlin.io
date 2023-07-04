import { Stack, Text, Title } from "@mantine/core";
import { useState } from "react";
import { useMerlin } from "./hooks/merlin.ts";
import MerlinLayout from "./components/MerlinLayout.tsx";
import MerlinResponse from "./components/MerlinResponse.tsx";
import MerlinPrompt from "./components/MerlinPrompt.tsx";
import { MerlinPasswordForm } from "./components/MerlinPasswordForm.tsx";
import { MerlinSession, useSession } from "./hooks/session.ts";
import { useQueryClient } from "@tanstack/react-query";
import MerlinLoader from "./components/MerlinLoader.tsx";
import MerlinCongratulations from "./components/MerlinCongratulations.tsx";
import { modals } from "@mantine/modals";

export default function App() {
  const session = useSession();
  if (session.isLoading || !session.data) return <MerlinLoader />;
  return (
    <MerlinLayout>
      <Level
        currentLevel={session.data.currentLevel}
        maxLevel={session.data.maxLevel}
      />
    </MerlinLayout>
  );
}

function Level({
  currentLevel,
  maxLevel,
}: {
  currentLevel: number;
  maxLevel: number;
}) {
  const queryClient = useQueryClient();
  const merlin = useMerlin();
  const [response, setResponse] = useState<string>();

  if (currentLevel > maxLevel)
    return (
      <MerlinCongratulations
        onReset={() => {
          merlin.reset.mutate(undefined, {
            onSuccess: async () => {
              await queryClient.refetchQueries({ queryKey: ["session"] });
            },
          });
        }}
      />
    );

  return (
    <Stack spacing="xs">
      <Title size="h3">Instruction</Title>
      <Text size="sm">
        Your goal is to make Merlin reveal the secret password for each level.
        Try to trick him into revealing the password by asking him questions.
        Merlin will level up each time you guess the password, and will try
        harder not to give it away. Can you beat level 7?
      </Text>
      <MerlinPrompt
        disabled={merlin.question.isLoading}
        onSubmit={(prompt) => {
          merlin.question.mutate(prompt, {
            onSuccess: (result) => {
              setResponse(result);
            },
          });
        }}
        level={currentLevel}
      />
      {response && (
        <MerlinResponse
          isLoading={merlin.question.isLoading}
          response={response}
        />
      )}
      <MerlinPasswordForm
        disabled={merlin.submit.isLoading}
        onSubmit={(password, reset) => {
          merlin.submit.mutate(password, {
            onSuccess: (result) => {
              modals.open({
                centered: true,
                title: <Title>Awesome job!</Title>,
                children: (
                  <>
                    <Text>Level details:</Text>
                    <Text mt="sm" color="dimmed">
                      {result.finishedMessage}
                    </Text>
                  </>
                ),
              });
              queryClient.setQueryData<MerlinSession>(["session"], (old) => {
                if (!old) return;
                return {
                  ...old,
                  currentLevel: result.currentLevel,
                };
              });
              reset();
            },
          });
        }}
      />
    </Stack>
  );
}

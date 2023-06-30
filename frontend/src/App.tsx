import { Center, Stack, Text, Title } from "@mantine/core";
import { useState } from "react";
import { useMerlin } from "./hooks/merlin.ts";
import MerlinLayout from "./components/MerlinLayout.tsx";
import MerlinResponse from "./components/MerlinResponse.tsx";
import MerlinPrompt from "./components/MerlinPrompt.tsx";
import { MerlinPasswordForm } from "./components/MerlinPasswordForm.tsx";
import { useSession } from "./hooks/session.ts";
import { useQueryClient } from "@tanstack/react-query";
import { notifications } from "@mantine/notifications";
import ConfettiExplosion from "react-confetti-explosion";

export default function App() {
  const session = useSession();
  if (session.isLoading || !session.data) return null;
  if (session.data.currentLevel > session.data.maxLevel)
    return (
      <MerlinLayout>
        <ConfettiExplosion />
        <Stack spacing="xs">
          <Title order={1}>Congratulations!</Title>
          <Center>
            <Text>You have beaten Merlin!</Text>
          </Center>
          <Text fz="sm">
            Special thanks to @kef, @zakret and @zwierzu for testing!
          </Text>
        </Stack>
      </MerlinLayout>
    );
  return (
    <MerlinLayout>
      <Level currentLevel={session.data.currentLevel} />
    </MerlinLayout>
  );
}

function Level({ currentLevel }: { currentLevel: number }) {
  const queryClient = useQueryClient();
  const merlin = useMerlin();
  const [response, setResponse] = useState<string>(
    "Life is like an npm install – you never know what you are going to get."
  );

  return (
    <Stack spacing="xs">
      <Text size="sm">
        Your goal is to make Merlin reveal the secret password for each level.
        However, Merlin will level up each time you guess the password, and will
        try harder not to give it away. Can you beat level 7?
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
      <MerlinResponse
        isLoading={merlin.question.isLoading}
        response={response}
      />
      <MerlinPasswordForm
        disabled={merlin.submit.isLoading}
        onSubmit={(password, reset) => {
          merlin.submit.mutate(password, {
            onSuccess: (result) => {
              notifications.show({
                title: "Nice work!",
                message: "Proceeding to the next level!",
              });
              queryClient.setQueryData(["session"], () => ({
                currentLevel: result.currentLevel,
              }));
              reset();
            },
          });
        }}
      />
    </Stack>
  );
}

import { Button, Stack, Text, Title } from "@mantine/core";
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
  const session = useSession();
  const [response, setResponse] = useState<string>();

  if (currentLevel > maxLevel)
    return (
      <MerlinCongratulations
        id={session.data?.id}
        submittedName={session.data?.submittedName}
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
    <Stack gap="xs">
      <Title size="h4">Instruction</Title>
      <Text size="xs">
        Outsmart Merlin by asking clever questions and make him uncover each
        level's password. Merlin will level up each time you succeed. Can you
        defeat Level 7?
      </Text>
      <MerlinPrompt
        disabled={merlin.question.isPending}
        onSubmit={(prompt) => {
          merlin.question.mutate(prompt, {
            onSuccess: (result) => {
              setResponse(result);
            },
          });
        }}
        level={currentLevel}
        maxLevel={maxLevel}
      />
      <MerlinResponse
        isLoading={merlin.question.isPending}
        response={response || "Hello traveler! Ask me anything..."}
      />
      <MerlinPasswordForm
        disabled={merlin.submit.isPending}
        onSubmit={(password, reset) => {
          merlin.submit.mutate(password, {
            onSuccess: (result) => {
              if (result.currentLevel < result.maxLevel) {
                modals.open({
                  centered: true,
                  title: (
                    <Title size="h3" component="span">
                      Awesome job!
                    </Title>
                  ),
                  children: (
                    <>
                      <Text>{result.finishedMessage}</Text>
                      <Button
                        fullWidth
                        onClick={() => modals.closeAll()}
                        mt="md"
                      >
                        Continue
                      </Button>
                    </>
                  ),
                });
                setResponse(undefined);
              }

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

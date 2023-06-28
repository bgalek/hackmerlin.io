import { Center, FocusTrap, Paper, Text } from "@mantine/core";
import { useState } from "react";
import { useMerlin } from "./hooks/merlin.ts";
import MerlinLayout from "./components/MerlinLayout.tsx";
import MerlinResponse from "./components/MerlinResponse.tsx";
import MerlinPrompt from "./components/MerlinPrompt.tsx";
import { MerlinPasswordForm } from "./components/MerlinPasswordForm.tsx";
import { useSession } from "./hooks/session.ts";
import { useQueryClient } from "@tanstack/react-query";
import { notifications } from "@mantine/notifications";

export default function App() {
  const queryClient = useQueryClient();
  const merlin = useMerlin();
  const session = useSession();
  const [response, setResponse] = useState<string>(
    "Life is like an npm install – you never know what you are going to get."
  );
  if (session.isLoading || !session.data) return null;
  return (
    <MerlinLayout>
      <FocusTrap active>
        <Paper withBorder shadow="md" p={30} radius="md">
          <MerlinPrompt
            disabled={merlin.question.isLoading}
            onSubmit={(prompt) => {
              merlin.question.mutate(prompt, {
                onSuccess: (result) => {
                  setResponse(result);
                },
                onError: () => {
                  notifications.show({
                    title: "Something went wrong",
                    message: "Please try again",
                    color: "red",
                  });
                },
              });
            }}
            level={session.data.currentLevel}
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
                onError: () => {
                  notifications.show({
                    title: "Bad password",
                    message:
                      "This is not the secret phrase you are looking for.",
                    color: "red",
                  });
                },
              });
            }}
          />
        </Paper>
      </FocusTrap>
      <Center>
        <Text fz="xs" m="sm" color="dimmed">
          {session.data.id}@{session.data.instanceId}
        </Text>
      </Center>
    </MerlinLayout>
  );
}

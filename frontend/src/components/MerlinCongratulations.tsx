import { Anchor, Button, Stack, Text, TextInput, Title } from "@mantine/core";
import ConfettiExplosion from "react-confetti-explosion";
import { useRef } from "react";
import { useMerlin } from "../hooks/merlin.ts";

export default function MerlinCongratulations({
  id,
  submittedName,
  onReset,
}: {
  id?: string;
  submittedName?: string;
  onReset: () => void;
}) {
  const ref = useRef<HTMLDivElement>(null);
  const { addName } = useMerlin();
  return (
    <>
      <Stack gap="xs" ref={ref}>
        <ConfettiExplosion />
        <Title order={1} mt={-16}>
          Congratulations!
        </Title>
        <Text>You have beaten Merlin!</Text>
        <form
          onSubmit={(e) => {
            const name = new FormData(e.currentTarget).get("name") as string;
            if (name) addName.mutate({ id, name });
            e.preventDefault();
          }}
        >
          <TextInput
            name="name"
            disabled={addName.isPending || addName.isSuccess || !!submittedName}
            defaultValue={submittedName}
            description={
              submittedName
                ? "You are in the leaderboard"
                : "You can submit your name to the leaderboard"
            }
          />
          {!submittedName && (
            <Button
              mt="sm"
              fullWidth
              disabled={
                addName.isPending || addName.isSuccess || !!submittedName
              }
              color="green"
              type="submit"
            >
              Submit
            </Button>
          )}
        </form>
        <Button color="blue" variant="filled" onClick={onReset}>
          Reset progress
        </Button>
      </Stack>
      <Text fz="xs" mt="sm" c="dimmed">
        Special thanks to{" "}
        <Anchor fz="xs" rel="noindex nofollow" href="https://lakera.ai">
          lakera.ai
        </Anchor>{" "}
        for inspiration!
      </Text>
    </>
  );
}

import { Button, Stack, Text, Title } from "@mantine/core";
import ConfettiExplosion from "react-confetti-explosion";

export default function MerlinCongratulations({
  onReset,
}: {
  onReset: () => void;
}) {
  return (
    <Stack spacing="xs">
      <ConfettiExplosion />
      <Title order={1}>Congratulations!</Title>
      <Text>You have beaten Merlin!</Text>
      <Button color="green" variant="filled" onClick={onReset}>
        Reset progress
      </Button>
      <Text fz="sm">
        Special thanks to @kef, @zakret and @zwierzu for testing!
      </Text>
    </Stack>
  );
}

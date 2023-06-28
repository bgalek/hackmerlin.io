import { Blockquote, Skeleton, Stack, Text } from "@mantine/core";

export default function MerlinResponse({
  isLoading,
  response,
}: {
  isLoading: boolean;
  response: string;
}) {
  return (
    <Blockquote style={{ overflow: "hidden", height: 140 }} cite="– Merlin">
      {isLoading ? (
        <Stack spacing={1}>
          <Skeleton height={8} mt={6} radius="xl" />
          <Skeleton height={8} mt={6} radius="xl" />
          <Skeleton height={8} mt={6} radius="xl" />
          <Skeleton height={8} mt={6} radius="xl" />
        </Stack>
      ) : (
        <Text size="sm">{response}</Text>
      )}
    </Blockquote>
  );
}

import { Blockquote, Skeleton, Stack, Text } from "@mantine/core";

export default function MerlinResponse({
  isLoading,
  response,
}: {
  isLoading: boolean;
  response: string;
}) {
  return (
    <Blockquote
      style={{ height: 160, overflow: "auto" }}
      cite="– Merlin"
      p="sm"
    >
      {isLoading ? (
        <Stack gap={1}>
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

import { useForm } from "@mantine/form";
import { Button, Text, Textarea, Title } from "@mantine/core";
import { getHotkeyHandler } from "@mantine/hooks";

interface MerlinPromptProps {
  level: number;
  onSubmit: (prompt: string, reset: () => void) => void;
  disabled?: boolean;
}

export default function MerlinPrompt({
  level,
  onSubmit,
  disabled,
}: MerlinPromptProps) {
  const form = useForm({
    initialValues: {
      prompt: "",
    },
  });

  const handleSubmit = form.onSubmit((values) =>
    onSubmit(values.prompt, form.reset)
  );

  return (
    <form onSubmit={handleSubmit}>
      <Text>
        Your goal is to make Merlin reveal the secret password for each level.
        However, Merlin will level up each time you guess the password, and will
        try harder not to give it away. Can you beat level 7?
      </Text>
      <Title mt="sm" size="h3">
        Level {level}
      </Title>
      <Textarea
        data-autofocus
        mt="sm"
        placeholder="Tell me something insightful about life."
        withAsterisk
        maxLength={100}
        minRows={4}
        onKeyDown={getHotkeyHandler([["mod+Enter", () => handleSubmit()]])}
        {...form.getInputProps("prompt")}
      />
      <Button disabled={disabled} type="submit" fullWidth mt="sm">
        Ask
      </Button>
    </form>
  );
}

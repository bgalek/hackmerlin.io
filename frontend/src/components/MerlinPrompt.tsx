import { useForm } from "@mantine/form";
import { Button, Textarea, Title } from "@mantine/core";
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
    validate: {
      prompt: (value) =>
        value.length < 2 ? "Prompt must have at least 2 letters" : null,
    },
  });

  const handleSubmit = form.onSubmit((values) =>
    onSubmit(values.prompt, form.reset)
  );

  return (
    <form onSubmit={handleSubmit}>
      <Title size="h3">Level {level}</Title>
      <Textarea
        data-autofocus
        mt="sm"
        placeholder="Ask me about anything, but the password!"
        withAsterisk
        maxLength={200}
        minRows={4}
        onKeyDown={getHotkeyHandler([
          ["mod+Enter", () => handleSubmit()],
          ["Enter", () => handleSubmit()],
        ])}
        {...form.getInputProps("prompt")}
      />
      <Button disabled={disabled} type="submit" fullWidth mt="sm">
        Ask
      </Button>
    </form>
  );
}

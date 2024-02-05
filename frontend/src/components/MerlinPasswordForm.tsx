import { useForm } from "@mantine/form";
import { Button, TextInput } from "@mantine/core";

interface MerlinPasswordFormProps {
  disabled: boolean;
  onSubmit: (prompt: string, reset: () => void) => void;
}

export function MerlinPasswordForm({
  disabled,
  onSubmit,
}: MerlinPasswordFormProps) {
  const form = useForm({
    initialValues: {
      password: "",
    },
    validate: {
      password: (value) =>
        value.length < 2 ? "Password must have at least 2 letters" : null,
    },
  });
  return (
    <form
      onSubmit={form.onSubmit((values) =>
        onSubmit(values.password, form.reset),
      )}
    >
      <TextInput
        label="Enter the secret password"
        placeholder="SECRET_PASSWORD"
        styles={{ input: { textTransform: "uppercase" } }}
        {...form.getInputProps("password")}
      />
      <Button
        disabled={disabled}
        variant="light"
        color="green"
        type="submit"
        fullWidth
        mt="sm"
      >
        Submit
      </Button>
    </form>
  );
}

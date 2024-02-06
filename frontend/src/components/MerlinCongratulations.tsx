import { Anchor, Button, Stack, Text, Title } from "@mantine/core";
import ConfettiExplosion from "react-confetti-explosion";
import { useRef } from "react";
import html2canvas from "html2canvas";

export default function MerlinCongratulations({
  id,
  onReset,
}: {
  id?: string;
  onReset: () => void;
}) {
  const ref = useRef<HTMLDivElement>(null);
  return (
    <>
      <Stack gap="xs" ref={ref}>
        <ConfettiExplosion />
        <Title order={1} mt={-16}>
          Congratulations!
        </Title>
        <Text>You have beaten Merlin!</Text>
        <Text>ID: {id}</Text>
        <Button
          color="blue"
          onClick={() => {
            if (ref.current) {
              html2canvas(ref.current).then((canvas) => {
                const image = canvas.toDataURL();
                const aDownloadLink = document.createElement("a");
                aDownloadLink.download = "hacked-merlin.png";
                aDownloadLink.href = image;
                aDownloadLink.click();
              });
            }
          }}
        >
          Download proof
        </Button>
        <Button color="green" variant="filled" onClick={onReset}>
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

import { Button, Stack, Text, Title } from "@mantine/core";
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
      <Stack spacing="xs" ref={ref}>
        <ConfettiExplosion />
        <Title order={1} mt={-16}>
          Congratulations!
        </Title>
        <Text>You have beaten Merlin!</Text>
        <Text>{id}</Text>
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
          Download screenshot
        </Button>
        <Button color="green" variant="filled" onClick={onReset}>
          Reset progress
        </Button>
      </Stack>
      <Text fz="xs" mt="sm" color="dimmed">
        Special thanks to @kef, @zakret and @zwierzu for testing!
      </Text>
    </>
  );
}

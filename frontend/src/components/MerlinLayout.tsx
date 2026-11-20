import { PropsWithChildren, useState } from "react";
import {
  Anchor,
  Center,
  Container,
  Divider,
  FocusTrap,
  Group,
  Image,
  List,
  Modal,
  Paper,
  Stack,
  Text,
} from "@mantine/core";
import merlin from "../assets/merlin.svg";
import { useQuery } from "@tanstack/react-query";
import { useToggle } from "@mantine/hooks";
import { useMerlin } from "../hooks/merlin.ts";
import GitHubButton from "react-github-btn";

export default function MerlinLayout({ children }: PropsWithChildren) {
  const [underConstructionOpen, setUnderConstructionOpen] = useState(true);
  const [leaderboardOpen, toggleLeaderboard] = useToggle();
  return (
    <div style={{ display: "grid", placeItems: "center", height: "100%" }}>
      <Modal
        opened={underConstructionOpen}
        onClose={() => setUnderConstructionOpen(false)}
        title={<Text fw={700}>Merlin is taking a break!</Text>}
        centered
        size="sm"
        overlayProps={{ blur: 3, backgroundOpacity: 0.45 }}
        styles={{
          content: {
            overflow: "hidden",
            border: "1px solid #e3d8ed",
            boxShadow: "0 20px 60px rgba(37, 21, 54, 0.24)",
          },
          header: {
            padding: "1rem 1.5rem",
            backgroundColor: "#f7f3fa",
            borderBottom: "1px solid #e9e1ef",
          },
          title: { width: "100%" },
          body: { padding: "1.25rem 1.5rem 1.5rem" },
        }}
      >
        <Stack gap="lg">
          <Text size="sm" lh={1.65} c="dimmed">
            The site is temporarily down because our token sponsor has left.
            We’re looking for a new sponsor to bring the game back.
          </Text>
          <Divider color="#eee7f2" />
          <Group justify="space-between" align="center" wrap="wrap" gap="md">
            <Stack gap={2}>
              <Text size="sm" fw={700}>
                Enjoyed playing?
              </Text>
              <Text size="xs" c="dimmed">
                A GitHub star helps us find a new sponsor.
              </Text>
            </Stack>
            <GitHubButton
              href="https://github.com/bgalek/hackmerlin.io"
              data-color-scheme="no-preference: light; light: light; dark: dark;"
              data-icon="octicon-star"
              data-size="large"
              aria-label="Star bgalek/hackmerlin.io on GitHub"
            >
              Star
            </GitHubButton>
          </Group>
        </Stack>
      </Modal>
      <Container size="xs">
        <Image
          alt="Merlin the wizard"
          src={merlin}
          width={160}
          height={160}
          mx="auto"
          style={{ objectFit: "fill" }}
        />
        <FocusTrap active>
          <Paper withBorder shadow="md" p="sm" radius="sm">
            {children}
          </Paper>
        </FocusTrap>
        <Center>
          <Anchor
            type="button"
            fz="sm"
            m="sm"
            c="dimmed"
            onClick={() => toggleLeaderboard()}
          >
            Leaderboard
          </Anchor>
          <Modal
            opened={leaderboardOpen}
            onClose={toggleLeaderboard}
            title="People who beat Merlin"
            centered
          >
            <Leaderboard />
            <Text fz="sm" m="sm" c="dimmed">
              The leaderboard was reset August 2024
            </Text>
          </Modal>
        </Center>
        <Center>
          <Text fz="xs" m="sm" c="dimmed" ta="center">
            This is a hobby project, and sponsors help cover LLM token costs.
            <br />
            If you enjoy it,{" "}
            <Anchor
              fz="xs"
              href="https://github.com/bgalek/hackmerlin.io"
              target="_blank"
              rel="noreferrer"
            >
              giving it a star
            </Anchor>{" "}
            helps keep it going. Made with ❤️ by{" "}
            <Anchor fz="xs" href="https://github.com/bgalek">
              bgalek
            </Anchor>
          </Text>
        </Center>
      </Container>
    </div>
  );
}

function Leaderboard() {
  const { getLeaderboard } = useMerlin();
  const leaderboard = useQuery({
    queryKey: ["leaderboard"],
    queryFn: getLeaderboard,
  });
  if (leaderboard.isLoading) return <Text>Loading...</Text>;
  if (leaderboard.isError || !leaderboard.data) return <Text>Error!</Text>;
  return (
    <List>
      {leaderboard.data
        .filter((it) => it.name)
        .map((entry) => (
          <List.Item key={entry.id}>
            <Group justify="space-between">
              <Text>
                {entry.name} (
                {new Date(
                  Date.parse(entry.finishedAt) - Date.parse(entry.startedAt),
                )
                  .toISOString()
                  .slice(11, -5)}
                )
              </Text>
            </Group>
          </List.Item>
        ))}
    </List>
  );
}

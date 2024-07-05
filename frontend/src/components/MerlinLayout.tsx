import { PropsWithChildren } from "react";
import {
  Anchor,
  Center,
  Container,
  FocusTrap,
  Group,
  Image,
  List,
  Modal,
  Paper,
  Text,
} from "@mantine/core";
import merlin from "../assets/merlin.svg";
import { useQuery } from "@tanstack/react-query";
import { useToggle } from "@mantine/hooks";
import { useMerlin } from "../hooks/merlin.ts";

export default function MerlinLayout({ children }: PropsWithChildren) {
  const [leaderboardOpen, toggleLeaderboard] = useToggle();
  return (
    <div style={{ display: "grid", placeItems: "center", height: "100%" }}>
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
            title="People who beaten Merlin"
            centered
          >
            <Leaderboard />
          </Modal>
        </Center>
        <Center>
          <Text fz="sm" m="sm" c="dimmed">
            made with ❤️ by{" "}
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

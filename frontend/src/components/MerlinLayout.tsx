import { PropsWithChildren } from "react";
import {
  Anchor,
  Center,
  Container,
  FocusTrap,
  Image,
  Paper,
  Text,
} from "@mantine/core";
import merlin from "../assets/merlin.svg";

export default function MerlinLayout({ children }: PropsWithChildren) {
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

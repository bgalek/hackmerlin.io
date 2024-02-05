import { PropsWithChildren } from "react";
import {
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
          src={merlin}
          width={180}
          height={180}
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
            made with ❤️ by bgalek
          </Text>
        </Center>
      </Container>
    </div>
  );
}

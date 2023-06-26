import { PropsWithChildren } from "react";
import { Container, Image } from "@mantine/core";
import merlin from "../assets/merlin.svg";

export default function MerlinLayout({ children }: PropsWithChildren) {
  return (
    <div style={{ display: "grid", placeItems: "center", height: "100%" }}>
      <Container size="xs">
        <Image src={merlin} width={240} height={320} mx="auto" />
        {children}
      </Container>
    </div>
  );
}

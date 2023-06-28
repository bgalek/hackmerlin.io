import { useMutation } from "@tanstack/react-query";
import wretch, { WretchError } from "wretch";
import { MerlinSession } from "./session.ts";
import { notifications } from "@mantine/notifications";

const api = wretch().errorType("json");

export function useMerlin() {
  return {
    question: useMutation({
      mutationFn: (prompt: string) =>
        api
          .url("/api/question")
          .headers({ "Content-Type": "text/plain" })
          .errorType("json")
          .post(prompt)
          .fetchError((err) => console.log(err))
          .text(),
      onError: (error: WretchError) => {
        notifications.show({
          title: error.json.error || error.response.statusText,
          message: error.json.message,
          color: "red",
        });
      },
    }),
    submit: useMutation({
      mutationFn: (password: string) =>
        api
          .url("/api/submit")
          .headers({ "Content-Type": "text/plain" })
          .post(password)
          .json<MerlinSession>(),
      onError: () => {
        notifications.show({
          title: "Bad password",
          message: "This is not the secret phrase you are looking for.",
          color: "red",
        });
      },
    }),
  };
}

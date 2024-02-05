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
          title: "Bad secret word",
          message: "This isn't the secret phrase you're looking for.",
          color: "red",
        });
      },
    }),
    reset: useMutation({
      mutationFn: () => api.url("/api/reset").post().res(),
      onSuccess: () => {
        notifications.show({
          title: "Your progress was reset.",
          message: "You'll now have to start over!",
          color: "blue",
        });
      },
    }),
  };
}

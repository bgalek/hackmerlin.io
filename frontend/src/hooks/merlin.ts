import { useMutation } from "@tanstack/react-query";
import wretch from "wretch";
import { MerlinSession } from "./session.ts";
import { notifications } from "@mantine/notifications";

interface ApiError {
  title: string;
  message?: string;
}

const api = wretch().customError<ApiError>(async (error, response) => {
  const json = await response.json();
  return {
    title: json.error || error.response.statusText,
    message: json.message,
  };
});

export function useMerlin() {
  return {
    question: useMutation({
      mutationFn: (prompt: string) =>
        api
          .url("/api/question")
          .headers({ "Content-Type": "text/plain" })
          .post(prompt)
          .text(),
      onError: (error: ApiError) => {
        notifications.show({
          title: error.title,
          message: error.message,
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
    addName: useMutation({
      mutationFn: ({ id, name }: { id?: string; name: string }) => {
        return api.url("/api/leaderboard/submit").post({ id, name }).res();
      },
      onSuccess: () => {
        notifications.show({
          title: "Your name was submitted.",
          message: "You'll now be on the leaderboard!",
          color: "blue",
        });
      },
      onError: () => {
        notifications.show({
          title: "Bad secret word",
          message: "This isn't the secret phrase you're looking for.",
          color: "red",
        });
      },
    }),
    getLeaderboard: () => {
      const data: Promise<LeaderboardEntry[]> = api
        .url("/api/leaderboard")
        .get()
        .json();
      return data;
    },
  };
}

interface LeaderboardEntry {
  id: string;
  name: string;
  startedAt: string;
  finishedAt: string;
}

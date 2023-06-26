import { useMutation } from "@tanstack/react-query";
import wretch from "wretch";

export function useMerlin() {
  return {
    question: useMutation({
      mutationFn: (prompt: string) =>
        wretch("/api/question")
          .headers({ "Content-Type": "text/plain" })
          .post(prompt)
          .text(),
    }),
    submit: useMutation({
      mutationFn: (password: string) =>
        wretch("/api/submit")
          .headers({ "Content-Type": "text/plain" })
          .post(password)
          .json<{ currentLevel: number }>(),
    }),
  };
}

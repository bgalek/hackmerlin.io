import { useQuery } from "@tanstack/react-query";

export interface MerlinSession {
  id: string;
  currentLevel: number;
  maxLevel: number;
  finishedMessage?: string;
  submittedName?: string;
}

export function useSession() {
  return useQuery<MerlinSession>({
    queryKey: ["session"],
    queryFn: () => fetch("/api/user").then((response) => response.json()),
  });
}

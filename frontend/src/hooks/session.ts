import { useQuery } from "@tanstack/react-query";

export function useSession() {
  return useQuery<{ id: string; currentLevel: number; maxLevel: number }>({
    queryKey: ["session"],
    queryFn: () => fetch("/api/user").then((response) => response.json()),
  });
}

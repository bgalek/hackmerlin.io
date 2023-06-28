import { useQuery } from "@tanstack/react-query";

export function useSession() {
  return useQuery<{
    id: string;
    currentLevel: number;
    maxLevel: number;
    instanceId: string;
  }>({
    queryKey: ["session"],
    queryFn: () => fetch("/api/user").then((response) => response.json()),
  });
}

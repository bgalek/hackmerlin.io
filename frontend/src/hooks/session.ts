import { useQuery } from "@tanstack/react-query";

export function useSession() {
  return useQuery<{ currentLevel: number }>({
    queryKey: ["session"],
    queryFn: () => fetch("/api/user").then((response) => response.json()),
  });
}

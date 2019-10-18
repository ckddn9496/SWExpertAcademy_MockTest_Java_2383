import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Solution {

	static class Person {
		int id;
		int x, y;
		int time;
		int decreasing;
		int[] sLen = new int[2];

		public Person(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}

	static class Stair {
		int x, y;
		int len;

		public Stair(int x, int y, int len) {
			this.x = x;
			this.y = y;
			this.len = len;
		}
	}

	static final int PERSON = 1;
	static final int MAX_STAIR_DECREASING = 3;

	static int N;
	static List<Person> people;
	static Stair[] stairs = new Stair[2];
	static boolean[] visit;

	static int minTime;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());

		for (int test_case = 1; test_case <= T; test_case++) {
			N = Integer.parseInt(br.readLine());

			people = new ArrayList<>();
			minTime = Integer.MAX_VALUE;

			StringTokenizer st;
			int pIdx = 0;
			int sIdx = 0;
			for (int i = 0; i < N; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < N; j++) {
					int v = Integer.parseInt(st.nextToken());
					if (v == PERSON) {
						people.add(new Person(pIdx++, i, j));
					} else if (v > PERSON) {
						stairs[sIdx++] = new Stair(i, j, v);
					}
				}
			}

			for (int i = 0; i < people.size(); i++) {
				for (sIdx = 0; sIdx < 2; sIdx++) {
					people.get(i).sLen[sIdx] = Math.abs(people.get(i).x - stairs[sIdx].x)
							+ Math.abs(people.get(i).y - stairs[sIdx].y);
				}
			}

			visit = new boolean[people.size()]; // for DFS
			dfs(0);

			System.out.println("#" + test_case + " " + minTime);
		}

	}

	private static void dfs(int n) {
		if (n == people.size()) {
			for (int i = 0; i < visit.length; i++) {
				if (visit[i]) {
					people.get(i).time = people.get(i).sLen[0] + 1;
					people.get(i).decreasing = stairs[0].len;
				} else {
					people.get(i).time = people.get(i).sLen[1] + 1;
					people.get(i).decreasing = stairs[1].len;
				}

			}
			run();
			return;
		}

		visit[n] = true;
		dfs(n + 1);

		visit[n] = false;
		dfs(n + 1);
	}

	private static void run() {
		PriorityQueue<Person> q = new PriorityQueue<>(new Comparator<Person>() {
			public int compare(Person p1, Person p2) {
				if (p1.time == p2.time) {
					return p1.decreasing - p2.decreasing;
				} else {
					return p1.time - p2.time;
				}

			}
		});

		q.addAll(people);
		int time = 0;

		int s1Count = 0;
		int s2Count = 0;
		while (!q.isEmpty()) {
			Person p = q.poll();
			if (p.time >= minTime) {
				return;
			}

			if (p.decreasing > 0) { // 내려가야함
				if (visit[p.id]) {
					if (s1Count < MAX_STAIR_DECREASING) {
						s1Count++;
						p.time += p.decreasing;
						p.decreasing = 0;
						q.add(p);
					} else {
						p.time++;
						q.add(p);
					}

				} else {
					if (s2Count < MAX_STAIR_DECREASING) {
						s2Count++;
						p.time += p.decreasing;
						p.decreasing = 0;
						q.add(p);
					} else {
						p.time++;
						q.add(p);
					}
				}
			} else { // 내려감
				if (time < p.time)
					time = p.time;
				if (visit[p.id])
					s1Count--;
				else
					s2Count--;
			}
		}

		if (time < minTime) {
			minTime = time;
		}

	}
}

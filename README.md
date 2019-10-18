# SWExpertAcademy_MockTest_Java_2383

## SW Expert Academy 2383. [모의 SW 역량테스트] 점심 식사시간

### 1. 문제설명

출처: https://swexpertacademy.com/main/code/problem/problemDetail.do?contestProbId=AV5-BEE6AK0DFAVl

input으로 맵의 한변의 길이 `N`이 들어오고 이어서 `N * N`지도의 정보가 들어온다. 지도에서 `1`은 사람 `0`은 빈공간 `2`이상은 계단을 의미한다. 계단은 반드시 `2`개 존재하며 사람들이 계단으로 가서 계단을 내려가 아래층으로 도착하는 시간의 최소 시간을 구하는 문제이다. 계단에는 최대 `3`명의 사람이 동시에 내려갈 수 있으며, 계단까지 가는거리는 지도에서 `x`축과 `y`축에 대해 `dx + dy`이다. 계단의 값은 계단을 내려가는데 걸리는 시간을 의미한다.

[제약 사항]

    1. 시간제한 : 최대 50개 테스트 케이스를 모두 통과하는데, C/C++/Java 모두 3초
    2. 방의 한 변의 길이 N은 4 이상 10 이하의 정수이다. (4 ≤ N ≤ 10)
    3. 사람의 수는 1 이상 10 이하의 정수이다. (1 ≤ 사람의 수 ≤ 10)
    4. 계단의 입구는 반드시 2개이며, 서로 위치가 겹치지 않는다.
    5. 계단의 길이는 2 이상 10 이하의 정수이다. (2 ≤ 계단의 길이 ≤ 10)
    6. 초기에 입력으로 주어지는 사람의 위치와 계단 입구의 위치는 서로 겹치지 않는다.


[입력]

> 입력의 맨 첫 줄에는 총 테스트 케이스의 개수 `T`가 주어지고, 그 다음 줄부터 `T`개의 테스트 케이스가 주어진다.
> 각 테스트 케이스의 첫 번째 줄에는 방의 한 변의 길이 `N`이 주어진다.
> 다음 N줄에는 `N*N` 크기의 지도의 정보가 주어진다.
> 지도에서 `1`은 사람을, `2` 이상은 계단의 입구를 나타내며 그 값은 계단의 길이를 의미한다.

[출력]

> 테스트 케이스의 개수만큼 T줄에 T개의 테스트 케이스 각각에 대한 답을 출력한다.
> 각 줄은 `#x`로 시작하고 공백을 하나 둔 다음 정답을 출력한다. (`x`는 `1`부터 시작하는 테스트 케이스의 번호이다)
> 정답은 이동이 완료되는 최소의 시간을 출력한다.

### 2. 풀이

input에 대하여 사람들의 번호와 위치를 저장하고, 계단에 대해서도 위치와 길이를 저장한다. 사람들은 `1`번 계단 또는 `2`번 계단을 이용할 수 있다. 이용할 계단에 대한 조합을 DFS를 이용하며 생성한 후 걸리는 시간을 측청하도록 하였다. 측정시 사람마다 이용할 계단이 결정되었으므로 `Person`클래스에서 시간에 대한 정보를 담는 `time`변수를 계단까지 가는 시간으로 지정한다. 또한, 내려가는데 걸리는 시간을 담은 `decreasing`변수를 선택한 계단의 길이로 지정해주었다.

시뮬레이션을 시작한다. `PriorityQueue`에 `Person`을 담아 `time`으로 정렬되도록 하였다. 이로 인해 먼저 수행해야할 사람에 대해 먼저 처리할 수 있도록 하였다. 두번째 정렬기준으로는 `decreasing`을 추가하였는데 구현중 계단을 내려가기 시작하면 `decreasing`을 `0`으로 만들고 도착했을때의 `time`으로 갱신하여 우선순위큐에 담았다. 그로인해 임의의 시간에 계단을 완전히 내려간사람에 대해 먼저 처리를 해주고 이어서 다른 사람이 계단에 들어갈 수 있도록 처리해 줄 수 있다. 큐를 이용한 BFS알고리즘으로 시뮬레이션을 진행하였고 문제의 조건에 맞추어 계단에 사람이 `3`명으로 꽉차서 이용하지 못할 경우 해당 계단에 대한 카운터를 증가시키고 시간을 `1`초 늘려주어 큐에 넣어 줌으로써 대기를 구현하였다. 큐가 빌때까지 위의 작업을 반복하며 마지막 사람의 행동은 아랫층에 도착한 행동이므로 이때의 시간이 완료시간이며 `minTime`을 갱신할 필요가 있다면 이때 갱신하여준다.

모든 시뮬레이션 도중 임의의 사람에 대해 수행중일때 그 사람의 `time`정보가 이전에 계산하여 나온 `minTime`보다 크다면 그 경우의 작업은 유망하지 않으므로 시뮬레이션을 종료하고 다음 시뮬레이션을 수행시킴으로써 시간을 줄일 수 있다.

```java

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


```


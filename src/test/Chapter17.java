package test;

import java.util.*;

public class Chapter17 {

    public static int n, m, result;
    public static int[] arr;
    public static Scanner sc = new Scanner(System.in);
    public static int[][] map;
    public static int[][] d;
    public static int INF = (int) 1e9;
    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};

    public static void main(String[] args) {
        _17_3();
    }

    static void _17_1() {

        n = sc.nextInt();
        m = sc.nextInt();

        map = new int[n + 1][n + 1];

        for (int i = 0; i <= n; i++) {
            Arrays.fill(map[i], INF);
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) map[i][j] = 0;
            }
        }

        for (int i = 0; i < m; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            int c = sc.nextInt();
            //비용중 최소값만 담기
            if (c < map[a][b]) map[a][b] = c;
        }

        _17_1_floyd();
        System.out.println();
    }

    static void _17_2() {
        n = sc.nextInt();
        m = sc.nextInt();

        map = new int[n + 1][n + 1];

        for (int i = 0; i <= n; i++) {
            Arrays.fill(map[i], INF);
        }


        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == j) map[i][j] = 0;
            }
        }

        for (int i = 0; i < m; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            map[a][b] = 1;
        }

        //플로이드 워셜을 사용하면 관련된 모든 값들의 흔적을 남길수 있음(관계가 있는지 없는지)
        _17_1_floyd();

        int result = 0;

        for (int i = 1; i <= n; i++) {
            int cnt = 0;

            for (int j = 1; j <= n; j++) {
                if (map[i][j] != INF || map[j][i] != INF) { //둘중에 하나가 값이 있다는건 교류가 있다는것 (연결되있다는것)
                    cnt++;
                }
            }
            if (cnt == n) {
                result++;
            }
        }

        System.out.println(result);
    }

    //실패 .. 오답
    static void _17_2_miss() {
        n = sc.nextInt();
        m = sc.nextInt();

        ArrayList<ArrayList<Integer>> list = new ArrayList<>();

        for (int i = 0; i <= n; i++) {
            list.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            list.get(a).add(b);
            list.get(b).add(a);
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                int idx = list.get(i).get(j);

                for (int k = 0; k < list.get(idx).size(); k++) {
                    if (!list.get(i).contains(list.get(idx).get(k))) list.get(i).add(list.get(idx).get(k));
                }
            }
        }
        System.out.println();
    }

    static void _17_1_floyd() {

        for (int k = 1; k <= n; k++) {
            for (int a = 1; a <= n; a++) {
                for (int b = 1; b <= n; b++) {
                    map[a][b] = Math.min(map[a][b], map[a][k] + map[k][b]);
                }
            }
        }
    }

    static void _17_3() {
        n = sc.nextInt();

        for (int t = 0; t < n; t++) {
            m = sc.nextInt();
            map = new int[m][m];
            boolean[][] visited = new boolean[m][m];

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    map[i][j] = sc.nextInt();
                }
            }
            result = INF;
            visited[0][0] = true;
            _17_3_dfs(0, 0, map[0][0], visited);
            System.out.println(result);
        }
    }

    static void _17_3_dfs(int x, int y, int sum, boolean[][] visited) {

        if (x == m - 1 && y == m - 1) {
            result = Math.min(result, sum);
            return;
        }

        for (int i = 0; i < 3; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (nx >= 0 && nx < m && ny >= 0 && ny < m && !visited[nx][ny]) {
                visited[nx][ny] = true;
                _17_3_dfs(nx, ny, sum + map[nx][ny], visited);
                visited[nx][ny] = false;
            }
        }
    }

    static void _17_3_sol() {

        n = sc.nextInt();

        for (int t = 0; t < n; t++) {

            m = sc.nextInt();
            map = new int[m][m];
            d = new int[m][m];

            for (int i = 0; i < m; i++) {
                Arrays.fill(d[i], INF);
            }

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    map[i][j] = sc.nextInt();
                }
            }
            result = INF;
            int x = 0 , y = 0;

            PriorityQueue<Node3> pq = new PriorityQueue<>();
            pq.offer(new Node3(x, y, map[x][y]));
            d[x][y] = map[x][y];

            while (!pq.isEmpty()) {

                Node3 node = pq.poll();
                int dist = node.distance; //현좌표의 거리값
                x = node.x;
                y = node.y;

                for (int i = 0; i < 4; i++) {

                    int nx = x + dx[i];
                    int ny = y + dy[i];

                    if (d[x][y] < dist) continue;

                    if (nx >= 0 && nx < m && ny >= 0 && ny < m) {
                        int cost = map[nx][ny] + dist; //다음좌표의 거리값 + 현좌표의 까지의 누적 거리값
                        if (cost < d[nx][ny]) { //다음 좌표의 최소 거리값 좌표
                            d[nx][ny] = cost;
                            pq.offer(new Node3(nx, ny, cost));
                        }
                    }
                }
            }
            System.out.println(d[m-1][m-1]);
        }
    }

    static void _17_4(){
        n = sc.nextInt();
        m = sc.nextInt();
        int[] d = new int[100];
        map = new int[100][100];

        ArrayList<ArrayList<Node>> list = new ArrayList<>();

        for(int i = 0; i <= n; i++){
            d[i] = INF;
        }

        for(int i = 0; i <= n ; i++){
            list.add(new ArrayList<>());
        }

        for(int i = 0 ; i < m; i++){
            int a = sc.nextInt();
            int b = sc.nextInt();
            list.get(a).add(new Node(b,1));
            list.get(b).add(new Node(a,1));
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(1,0));
        d[1] = 0;

        while(!pq.isEmpty()){

            Node node = pq.poll();
            int now = node.getIndex();
            int dist = node.getDistance();

            if(d[now] < dist) continue;

            for(int i = 0 ; i < list.get(now).size(); i++){
                int cost = d[now] + list.get(now).get(i).getDistance();

                if(cost < d[list.get(now).get(i).getIndex()]){
                    d[list.get(now).get(i).getIndex()] = cost;
                    pq.offer(new Node(list.get(now).get(i).getIndex() , cost));
                }
            }
        }
        int idx = 1;
        int count = 0;
        int distance = 1;

        for(int i  = 2; i <= n; i++){
            if(d[idx] >= d[i]){
                count++;
            }else{
                idx = i;
                count = 1;
            }
            distance = d[i];
        }
        System.out.println(idx + " " + distance + " " + count);
    }
}

class Node3 implements Comparable<Node3>{
    int x;
    int y;
    int distance;

    public Node3(int x, int y, int distance) {
        this.x = x;
        this.y = y;
        this.distance = distance;
    }

    @Override
    public int compareTo(Node3 o) {
        return distance- o.distance;
    }
}

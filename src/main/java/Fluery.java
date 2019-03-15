import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bartek on 09.03.2019.
 */

//dla grafów nieskierowanych
public class Fluery {
    private Map<String, ArrayList<String>> adjList;


    private Map<String, ArrayList<String>> adj; // lista przylegania dla nieskierowanego

    private String textForGui;

    public Fluery(Map<String, ArrayList<String>> adjList) {
        this.adjList = new HashMap<String, ArrayList<String>>(adjList);
        init();
    }

    private void init() {
        if(adjList == null && adjList.isEmpty()) {
            return;
        }

        adj = new HashMap<String, ArrayList<String>>();

        for (Map.Entry<String, ArrayList<String>> entry : adjList.entrySet()) {
            for(int i=0; i<entry.getValue().size(); i++) {
                addEdge(entry.getKey(), entry.getValue().get(i));
            }
        }
    }

    private void addEdge(String u, String v)
    {
        if (adj.get(u) == null) {
            adj.put(u, new ArrayList<String>());
            adj.get(u).add(v);
        } else {
            adj.get(u).add(v);
        }

        if (adj.get(v) == null) {
            adj.put(v, new ArrayList<String>());
            adj.get(v).add(u);
        } else {
            adj.get(v).add(u);
        }
    }

    private void removeEdge(String u, String v)
    {
        adj.get(u).remove(v);
        adj.get(v).remove(u);

    }

    public void printEulerTour()
    {
        //szukanie wierzcholka nieparzystego stopnia
        textForGui = new String();

        String vertex = new String();

        for (String key : adj.keySet()) {
            vertex = key;
            if (adj.get(key).size() % 2 == 1) {
                vertex = key;
                break;
            }
        }

        int res = isEulerian();
        if (res == 0) {
            System.out.println("Graf nie jest Eulerowski");
            textForGui += "Graf nie jest Eulerowski";
        }
        else if (res == 1) {
            System.out.println("raf zawiera ścieżkę Eulera:");
            textForGui += "Graf zawiera ścieżkę Eulera";
            printEulerUtil(vertex);
        }
        else
        {
            System.out.println("Grafa zawiera cykl Eulera");
            textForGui += "Grafa zawiera cykl Eulera:";
            printEulerUtil(vertex);
        }
        System.out.println();
    }

    private void printEulerUtil(String u)
    {
        textForGui += "\n";
        for (int i = 0; i < adj.get(u).size(); i++)
        {
            String v = adj.get(u).get(i);
            if (isValidNextEdge(u, v))
            {
                System.out.print(u + "-" + v + " ");
                textForGui += u + "-" + v + " ";


                removeEdge(u, v);
                printEulerUtil(v);
            }
        }
    }

    private boolean isValidNextEdge(String u, String v)
    {

        if (adj.get(u).size() == 1) {
            return true;
        }

        Map<String, Boolean> isVisited;
        isVisited= isVisited(adj);
        int count1 = dfsCount(u, isVisited);

        removeEdge(u, v);
        isVisited = isVisited(adj);
        int count2 = dfsCount(u, isVisited);


        addEdge(u, v);
        return (count1 > count2) ? false : true;
    }

    //dfs
    private int dfsCount(String v, Map<String, Boolean> isVisited)
    {
        isVisited.put(v, true);
        int count = 1;
        for (String adj : adj.get(v))
        {
            if (!isVisited.get(adj))
            {
                count = count + dfsCount(adj, isVisited);
            }
        }
        return count;
    }


    private Map<String, Boolean> isVisited(Map<String, ArrayList<String>> adj) {
        Map<String, Boolean> isVisited;
        isVisited = new HashMap<String, Boolean>();
        for (Map.Entry entry : adj.entrySet()) {
            isVisited.put(entry.getKey().toString(), false);
        }
        return isVisited;
    }

    private boolean isConnected()
    {
        Map<String, Boolean> isVisited = isVisited(adj);
        int i = 0;
        String key_check = "";

        for(String key : adj.keySet()){
            if(adj.get(key).size() != 0) {
                key_check = key;
                break;
            }
            i++;
        }

        if (i == adj.size())
            return true;


        dfsCount(key_check, isVisited);

        for (String key : adj.keySet()) {
            if(isVisited.get(key) == false && adj.get(key).size() > 0) {
                return false;
            }
        }

        return true;
    }

    private int isEulerian()
    {
        if (isConnected() == false)
            return 0;

        int odd = 0;
        for (String key : adj.keySet())
            if (adj.get(key).size()%2!=0) {
                odd++;
            }

        if (odd > 2)
            return 0;

        return (odd==2)? 1 : 2;
    }

    public String getTextForGui() {
        return textForGui;
    }
}

import java.util.*;

public class Main {
    static Stack<Integer> tower1Global;
    static Stack<Integer> tower2Global;
    static Stack<Integer> tower3Global;

    public static void main(String[] args) {
        String[] listOfString = {"bible", "lebib", "eibbl", "ooze", "solubb", "zooe", "bulbos"};
        System.out.println(anagramGrouper(listOfString));

        towerOfHanoi();
    }

    private static Collection<ArrayList<String>> anagramGrouper(String[] strs) {
        HashMap<String, ArrayList<String>> seenAnagram = new HashMap<>();

        for (String str : strs) {
            char[] characterAnagram = str.toCharArray();

            ArrayList<Character> characterAnagramList = new ArrayList<>();
            for(char character : characterAnagram) {
                characterAnagramList.add(character);
            }
            Collections.sort(characterAnagramList);

            StringBuilder sb = new StringBuilder();
            for (char character : characterAnagramList) {
                sb.append(character);
            }

            String generalAnagramWord = sb.toString();

            if (seenAnagram.containsKey(generalAnagramWord)) {
                ArrayList<String> currentStringList = seenAnagram.get(generalAnagramWord);
                currentStringList.add(str);
                seenAnagram.put(generalAnagramWord, currentStringList);
            } else {
                ArrayList<String> listOfOnlyAnagramSoFar = new ArrayList<>();
                listOfOnlyAnagramSoFar.add(str);
                seenAnagram.put(generalAnagramWord,  listOfOnlyAnagramSoFar);
            }
        }
        return seenAnagram.values();
    }

    private static void towerOfHanoi() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of rings:");

        int initialRingCount = sc.nextInt();

        while (initialRingCount <= 0) {
            System.out.println("Enter number of rings:");
            initialRingCount = sc.nextInt();
        }

        Stack<Integer> completedTower = initializeCompleteTower(initialRingCount);
        Stack<Integer> tower1 = initializeCompleteTower(initialRingCount);
        Stack<Integer> tower2 = new Stack<>();
        Stack<Integer> tower3 = new Stack<>();

        tower1Global = tower1;
        tower2Global = tower2;
        tower3Global = tower3;

        constructTowerString(initialRingCount, tower1Global, tower2Global, tower3Global);

        if (initialRingCount % 2 == 1) {
            clearTowerForOddRings(initialRingCount, completedTower, tower1, tower2, tower3);
        } else {
            clearTowerForEvenRings(initialRingCount, completedTower, tower1, tower2, tower3);
        }
    }

    private static void clearTowerForOddRings(int initialRingCount,
                                              Stack<Integer> completedTower,
                                              Stack<Integer> tower1, Stack<Integer> tower2, Stack<Integer> tower3) {
        while (!tower3.equals(completedTower)) {
            boolean canClearTower1 = clearOutTower(initialRingCount, tower1.size(), tower1, tower2, tower3);

            boolean canClearTower2 = clearOutTower(initialRingCount, tower2.size(), tower2, tower1, tower3);

            if (!canClearTower1 && !canClearTower2) {
                clearOutTower(initialRingCount, tower3.size(), tower3, tower1, tower2);
            }
        }
    }

    private static void clearTowerForEvenRings(int initialRingCount,
                                               Stack<Integer> completedTower,
                                               Stack<Integer> tower1, Stack<Integer> tower2, Stack<Integer> tower3) {
        while (!tower3.equals(completedTower)) {
            boolean canClearTower2 = clearOutTower(initialRingCount, tower2.size(), tower2, tower1, tower3);
            boolean canClearTower1 = clearOutTower(initialRingCount, tower1.size(), tower1, tower2, tower3);

            if (!canClearTower1 && !canClearTower2) {
                clearOutTower(initialRingCount, tower3.size(), tower3, tower1, tower2);
            }
        }
    }

    private static boolean clearOutTower(int initialRingCount, int ringCount,
                                         Stack<Integer> startTower,
                                         Stack<Integer> placeholderTower,
                                         Stack<Integer> endTower) {
        while (ringCount > 0) {
            int topRingValueStack1 = startTower.peek();
            if (ringCount % 2 == 0) {
                int topRingValueStackPlaceholder = stackPeekOrPositiveInfinity(placeholderTower);

                if (topRingValueStack1 < topRingValueStackPlaceholder) {
                    placeholderTower.add(startTower.pop());
                    ringCount--;

                    constructTowerString(initialRingCount, tower1Global, tower2Global, tower3Global);
                } else {
                    return false;
                }
            } else {
                int topRingValueStackEnd = stackPeekOrPositiveInfinity(endTower);

                if (topRingValueStack1 < topRingValueStackEnd) {
                    endTower.add(startTower.pop());
                    ringCount--;

                    constructTowerString(initialRingCount, tower1Global, tower2Global, tower3Global);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private static int stackPeekOrPositiveInfinity(Stack<Integer> stack) {
        int peekValue;
        try {
            peekValue = stack.peek();
        } catch(Exception e) {
            peekValue = (int) Double.POSITIVE_INFINITY;
        }
        return peekValue;
    }

    private static void constructTowerString(int ringCount,
                                             Stack<Integer> tower1, Stack<Integer> tower2, Stack<Integer> tower3) {
        StringBuilder sb = new StringBuilder();

        sb.append("------\n");
        for (int i=ringCount - 1; i >= 0; i--) {
            sb.append(getOrReturnZero(tower1, i) + " ");
            sb.append(getOrReturnZero(tower2, i) + " ");
            sb.append(getOrReturnZero(tower3, i) + " \n");
        }
        sb.append("------\n");

        System.out.println(sb);
    }

    private static int getOrReturnZero(Stack<Integer> tower, int index) {
        try {
            return tower.get(index);
        } catch (Exception e) {
            return 0;
        }
    }

    private static Stack<Integer> initializeCompleteTower(int ringCount) {
        Stack<Integer> completedTower = new Stack<>();
        for (int i=ringCount; i > 0; i--) {
            completedTower.push(i);
        }
        return completedTower;
    }
}

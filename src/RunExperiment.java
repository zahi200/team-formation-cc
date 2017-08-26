import java.util.*;

public class RunExperiment {

    private static Set<Expert> experts = new HashSet<Expert>();
    private static Set<Communication> communications = new HashSet<Communication>();
    private static Expert leader = null;
    private static Set<Skill> requiredSkills = new HashSet<Skill>();

    private static List<Expert> team =  new LinkedList<Expert>();
    private static Set<Expert> availableExperts = new HashSet<Expert>();
    private static Set<Skill> uncoveredSkills = new HashSet<Skill>();
    private static double[] distancesFromLeader = null;

    public static void main(String[] args) {
        init();
        teamMinLeaderAlgorithm();
        //result: team
        System.out.println(team);
    }

    private static void teamMinLeaderAlgorithm() {
        if(expertsCanCoveredSkills()){
            updateSelectedExpert(leader);
            while(!uncoveredSkills.isEmpty()){
                Expert selectedExpert = selectExpertThatMinimizedCost();
                updateSelectedExpert(selectedExpert);
            }
        }
    }

    private static boolean expertsCanCoveredSkills() {
        Set<Skill> expertsSkills = new HashSet<Skill>();
        for(Expert expert: experts){
            expertsSkills.addAll(expert.getSkills());
        }
        Set<Skill> cloneRequiredSkills = new HashSet<Skill>(requiredSkills);
        cloneRequiredSkills.removeAll(expertsSkills);
        return cloneRequiredSkills.size() == 0;
    }

    private static Expert selectExpertThatMinimizedCost() {
        Expert selectedExpert = null;
        double lowestCost = Double.POSITIVE_INFINITY;
        for(Expert expert : availableExperts){
            double communicationCostWithLeader = getCommunicationCostWithLeader(expert);
            int numOfRelevantSkills = getNumOfRelevantSkills(expert);
            if(numOfRelevantSkills == 0){
                availableExperts.remove(expert); //this expert in no more relevant
                continue;
            }
            double cost = communicationCostWithLeader / numOfRelevantSkills;
            if(cost < lowestCost){
                selectedExpert = expert;
                lowestCost = cost;
            }
        }
        return selectedExpert;
    }

    private static int getNumOfRelevantSkills(Expert expert) {
        Set<Skill> skills = expert.getRelevantSkills();
        skills.retainAll(uncoveredSkills);
        return skills.size();
    }

    private static double getCommunicationCostWithLeader(Expert expert) {
        return distancesFromLeader[expert.getSerialNumber()];
    }

    private static void updateSelectedExpert(Expert expert) {
        team.add(expert);
        availableExperts.remove(expert);
        uncoveredSkills.removeAll(expert.getSkills());
    }


    private static void init() {
        initData();
        availableExperts.addAll(experts);
        uncoveredSkills.addAll(requiredSkills);
        calcCommunicationsWithLeader();
    }

    private static void calcCommunicationsWithLeader() {
        FloydWarshall floydWarshall = new FloydWarshall(experts.size());
        for(Communication communication : communications){
            floydWarshall.addEdge(communication.getE1().getSerialNumber(), communication.getE2().getSerialNumber(), communication.getCost());
        }
        double[][] distances = floydWarshall.floydWarshall();
        distancesFromLeader = distances[leader.getSerialNumber()];
    }

    private static void initData() {
        Expert e1 = new Expert(1);
        Expert e2 = new Expert(2);
        Expert e3 = new Expert(3);
        e1.setIsLeader(true);
        leader = e1;
        experts.add(e1);
        experts.add(e2);
        experts.add(e3);
        communications.add(new Communication(e1, e2, 3));
        communications.add(new Communication(e1, e3, 2));
        Skill s1 = new Skill();
        Skill s2 = new Skill();
        Skill s3 = new Skill();
        e1.addSkill(s1);
        e1.addSkill(s2);
        e2.addSkill(s3);
        e3.addSkill(s2);
        e3.addSkill(s3);
        requiredSkills.add(s1);
        requiredSkills.add(s2);
    }
}

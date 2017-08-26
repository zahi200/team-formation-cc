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
        Set<Expert> unrelevantExperts = new HashSet<Expert>();
        for(Expert expert : availableExperts){
            double communicationCostWithLeader = getCommunicationCostWithLeader(expert);
            int numOfRelevantSkills = getNumOfRelevantSkills(expert);
            if(numOfRelevantSkills == 0){
                unrelevantExperts.add(expert); //this expert in no more relevant
                continue;
            }
            double cost = communicationCostWithLeader / numOfRelevantSkills;
            if(cost < lowestCost){
                selectedExpert = expert;
                lowestCost = cost;
            }
        }
        availableExperts.removeAll(unrelevantExperts);
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
        Expert eL = new Expert("eL");
        Expert e2 = new Expert("e2");
        Expert e3 = new Expert("e3");
        Expert e4 = new Expert("e4");
        eL.setIsLeader(true);
        leader = eL;
        experts.add(eL);
        experts.add(e2);
        experts.add(e3);
        experts.add(e4);
        communications.add(new Communication(eL, e2, 3));
        communications.add(new Communication(e2, e3, 2));
        communications.add(new Communication(e2, e4, 4));
        Skill s1 = new Skill();
        Skill s2 = new Skill();
        Skill s3 = new Skill();
        Skill s4 = new Skill();
        Skill sL = new Skill();
        eL.addSkill(sL);
        e2.addSkill(s1);
        Skill[] e3Skills = {s1, s2};
        e3.addSkills(e3Skills);
        Skill[] e4Skills = {s3, s4};
        e4.addSkills(e4Skills);
        requiredSkills.add(s1);
        requiredSkills.add(s2);
        requiredSkills.add(s3);
        requiredSkills.add(s4);
        requiredSkills.add(sL);

    }
}

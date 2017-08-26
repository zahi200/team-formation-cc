import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Expert implements Comparable<Expert>{

    private final int id;
    private final int serialNumber;
    private static int counter = 0;
    private boolean isLeader;
    private Set<Skill> skills = new HashSet<Skill>();
    private Set<Skill> relevantSkills = null;

    public Expert(int id) {
        this.id = id;
        serialNumber = counter;
        counter ++;

    }

    public void setIsLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    @Override
    public int compareTo(Expert expert) {
        return expert.getSerialNumber() - this.getSerialNumber();
    }

    public Set<Skill> getRelevantSkills() {
        if(relevantSkills == null){
            relevantSkills = new HashSet<Skill>(skills);
        }
        return relevantSkills;
    }

    public void resetRelevantSkills() {
        relevantSkills = null;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }
}

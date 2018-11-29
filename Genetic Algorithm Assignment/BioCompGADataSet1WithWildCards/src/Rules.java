
/**
 *
 * @author Thomas Hughes
 */
public class Rules 
{

    private int action = 0;
    private int[] condition;

    public Rules(int rSize) 
    {
        this.condition = new int[rSize];
    }

    public int getAction() 
    {
        return action;
    }

    public void setAction(int act) 
    {
        this.action = act;
    }

    public int[] getCondition() 
    {
        return condition;
    }

    public void setCondition(int[] cond) 
    {
        this.condition = cond;
    }
}

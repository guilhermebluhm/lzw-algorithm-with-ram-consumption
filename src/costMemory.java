public class costMemory {

    public static Long comsumptionMemoryLzw(){
        return Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
    }

}

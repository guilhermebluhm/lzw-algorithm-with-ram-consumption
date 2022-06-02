import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class compressao {

    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
            OperatingSystemMXBean.class);

    public List<Integer> compressData(String uncompressString){
        int BIT_RANGE_DECODE = 255;
        Map<String, Integer> TABLE_ENCODED_MESSAGE = new HashMap<>();
        for(int i = 0 ; i < BIT_RANGE_DECODE ; i+=1){
            TABLE_ENCODED_MESSAGE.put("" + (char) i, i);
        }
        String initial_state = "";
        List<Integer> position_ocurrence_compress = new ArrayList<>();
        //inicio verificacao metricas
        long startPercCpuCost = osBean.getProcessCpuTime();
        long start = System.currentTimeMillis();
        long beforeUsedMem=costMemory.comsumptionMemoryLzw();
        //...
        for(char c : uncompressString.toCharArray()){
            String new_ocurrence = initial_state + c;
            if(TABLE_ENCODED_MESSAGE.containsKey(new_ocurrence)){
                initial_state = new_ocurrence;
            }
            else{
                position_ocurrence_compress.add(TABLE_ENCODED_MESSAGE.get(initial_state));
                TABLE_ENCODED_MESSAGE.put(new_ocurrence, BIT_RANGE_DECODE+=1);
                initial_state = "" + c;
            }
            for (Map.Entry<String, Integer> itr: TABLE_ENCODED_MESSAGE.entrySet()) {
                String v = itr.getKey();
                Integer i = itr.getValue();

                if(v.equals(new_ocurrence.replace(" ", ""))){
                    System.out.println("string identificada baseada na codificacao: " + v + " entrada associada: " + i);
                }
            }
        }
        if(!initial_state.equals("")){
            position_ocurrence_compress.add(TABLE_ENCODED_MESSAGE.get(initial_state));
        }
        //encerramento verificacao metricas
        long endCostCpuLoad = osBean.getProcessCpuTime();
        long afterUsedMem=costMemory.comsumptionMemoryLzw();
        long end = System.currentTimeMillis();
        //...
        System.out.println("Tempo de Execucao: " + (end - start) + "ms");
        System.out.println("Ciclos de CPU: " + (endCostCpuLoad - startPercCpuCost));
        System.out.println("Memoria usada : " + valueCostMemory.
                totalCostMemory(beforeUsedMem,afterUsedMem)/1024 + "KB");
        return position_ocurrence_compress;
    }

}

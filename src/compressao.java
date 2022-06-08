import com.sun.management.OperatingSystemMXBean;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class compressao {

    Map<String, Integer> TABLE_ENCODED_MESSAGE = new HashMap<>();
    List<Integer> position_ocurrence_compress = new ArrayList<>();
    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
            OperatingSystemMXBean.class);

    public List<Integer> compressData(String uncompressString){
        //gera o range de bits "default" para a ASCII
        //popula o dicionário com as respectivas entradas char + int
        int BIT_RANGE_DECODE = 255;

        for(int i = 0 ; i < BIT_RANGE_DECODE ; i+=1){
            TABLE_ENCODED_MESSAGE.put("" + (char) i, i);
        }
        //define o string de rastreamento que define para uma nova
        //recontagem para casos de um novo encadeamento de substrings
        String initial_state = "";
        //a lista de entrada de correspondencia dicionário-codificação
        //para quando ocorrer o passo de decodificação se basear na correspodencia
        //caractere - valor

        //inicio verificacao metricas
        long startPercCpuCost = osBean.getProcessCpuTime();
        long start = System.currentTimeMillis();
        long beforeUsedMem=costMemory.comsumptionMemoryLzw();
        //...
        for(char c : uncompressString.toCharArray()){
            //procura fazer a montagem para a string dada como input
            //utilizando como referencia a string vazia e um segmento do que foi passado
            String new_ocurrence = initial_state + c;
            if(TABLE_ENCODED_MESSAGE.containsKey(new_ocurrence)){
                //caso já exista no dicionário aquela ocorrência somente verifica a atualiza a
                //entrada para seguir na busca de novas correspondencias
                initial_state = new_ocurrence;
            }
            else{
                //para caso de nao haver a substring via compactação da entrada na lista
                //que serve para o passo da decodificação
                position_ocurrence_compress.add(TABLE_ENCODED_MESSAGE.get(initial_state));
                //insere no dicionário a string que contem a montagem a construção do chartToArray
                TABLE_ENCODED_MESSAGE.put(new_ocurrence, BIT_RANGE_DECODE+=1);
                //e retoma a montagem assumindo a string de apoio como zerada e retoma a procura de
                //uma nova substring
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
            //caso que deve ocorrer pelo menos uma unica vez dada a situação de uma
            //unica ocorrencia para uma unica substring identificada pois para a logica
            //implementada o else sera a primeira chamada a ser satisfeita
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

    public void readFile(String fileName) throws FileNotFoundException, IOException {
        File file = new File(fileName);
        byte[] data;
        try (FileInputStream fis = new FileInputStream(file)) {
            data = new byte[(int) file.length()];
            fis.read(data);
        }
        String text = new String(data, "UTF-8");
        compressData(text);
    }
}

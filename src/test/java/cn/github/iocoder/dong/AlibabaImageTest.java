package cn.github.iocoder.dong;

import com.alibaba.dashscope.aigc.imagesynthesis.*;
import com.alibaba.dashscope.task.AsyncTaskListParam;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlibabaImageTest {
    public static void basicCall() throws ApiException, NoApiKeyException {
        ImageSynthesis is = new ImageSynthesis();
        ImageSynthesisParam build = ImageSynthesisParam.builder()
                .model(ImageSynthesis.Models.WANX_V1).n(4)
                .size("1024*1024").prompt("女孩在沙滩晒太阳，旁边是一望无际的大海").build();
//        ImageSynthesisParam param =
//                ImageSynthesisParam.builder()
//                        .model(ImageSynthesis.Models.WANX_V1)
//                        .n(4)
//                        .size("1024*1024")
//                        .prompt("雄鹰自由自在的在蓝天白云下飞翔")
//                        .build();

        ImageSynthesisResult result = is.asyncCall(build);
//        String taskId = result.getOutput().getTaskId();
        System.out.println("result="+result);
        ImageSynthesisResult fetch = is.fetch(result, null);
        System.out.println("fetch="+fetch);
        ImageSynthesisResult wait = is.wait(result, null);
        System.out.println("wait="+wait);
    }

    public static void listTask() throws ApiException, NoApiKeyException {
        ImageSynthesis is = new ImageSynthesis();
        AsyncTaskListParam param = AsyncTaskListParam.builder().build();
        ImageSynthesisListResult result = is.list(param);
        System.out.println(result);
    }

    public static void fetchTask(String taskId) throws ApiException, NoApiKeyException {
        ImageSynthesis is = new ImageSynthesis();
        // If set DASHSCOPE_API_KEY environment variable, apiKey can null.
        ImageSynthesisResult result = is.fetch(taskId, null);
        System.out.println(result.getOutput());
        System.out.println(result.getUsage());
    }

    public static void main(String[] args){
        try{
            Constants.apiKey = "";
            basicCall();
            //listTask();
        }catch(ApiException|NoApiKeyException e){
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }
}
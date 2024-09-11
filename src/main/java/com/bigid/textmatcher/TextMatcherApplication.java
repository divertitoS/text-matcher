package com.bigid.textmatcher;

import com.bigid.textmatcher.model.Batch;
import com.bigid.textmatcher.model.Offset;
import com.bigid.textmatcher.service.AggregatorService;
import com.bigid.textmatcher.service.BatchHandler;
import com.bigid.textmatcher.service.FileReader;
import com.bigid.textmatcher.util.Constants;
import com.bigid.textmatcher.util.args.options.ApplicationOptionHandler;
import com.bigid.textmatcher.util.args.options.ApplicationOptionHandlerStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.bigid.textmatcher.util.Constants.SOURCE_PATH;

@SpringBootApplication
@AllArgsConstructor
public class TextMatcherApplication implements ApplicationRunner {
	private final FileReader fileReader;
	private final BatchHandler batchHandler;
	private final AggregatorService aggregatorService;
	private final ApplicationOptionHandlerStrategy applicationOptionHandlerStrategy;

    public static void main(String[] args) {
		SpringApplication.run(TextMatcherApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		initializeOptions(args);
		BlockingQueue<Batch> batches = new ArrayBlockingQueue<>(Constants.CONCURRENT_MATCHERS_NUMBER);

		Queue<Map<String, List<Offset>>> matches = batchHandler.handle(batches);
		fileReader.read(SOURCE_PATH, batches);
		aggregatorService.aggregate(matches);
	}

	private void initializeOptions(ApplicationArguments args) {
		ApplicationOptionHandler applicationOptionHandler;

		for (String optionName : args.getOptionNames()) {
			String optionValue = args.getOptionValues(optionName).get(0);

			applicationOptionHandler = applicationOptionHandlerStrategy.getHandler(optionName);
			applicationOptionHandler.handle(optionValue);
		}

	}
}

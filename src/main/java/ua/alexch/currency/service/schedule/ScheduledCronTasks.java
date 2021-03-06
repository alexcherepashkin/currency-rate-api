package ua.alexch.currency.service.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import ua.alexch.currency.service.RateDataService;
import ua.alexch.currency.service.RateSource;

@Component
public class ScheduledCronTasks {
    @Autowired
    @Qualifier("monobankSource")
    private RateSource monobankSource;

    @Autowired
    @Qualifier("privatbankSource")
    private RateSource privatbankSource;

    @Autowired
    @Qualifier("minfinSource")
    private RateSource minfinSource;

    @Autowired
    private RateDataService rateService;

    @Value("${source.monobank.cron}")
    private String cronMB;

    @Value("${source.privatbank.cron}")
    private String cronPB;

    @Value("${source.minfin.cron}")
    private String cronMF;

    @Bean
    public CommandLineRunner commandLineRunner(DownloadTaskScheduler scheduler) {
        return args -> {
            scheduler.scheduleCronTask(new DownloadTask(monobankSource, rateService), cronMB);
            scheduler.scheduleCronTask(new DownloadTask(privatbankSource, rateService), cronPB);
            scheduler.scheduleCronTask(new DownloadTask(minfinSource, rateService), cronMF);
        };
    }
}

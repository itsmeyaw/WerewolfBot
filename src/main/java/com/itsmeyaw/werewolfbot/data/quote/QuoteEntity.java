package com.itsmeyaw.werewolfbot.data.quote;

import javax.persistence.*;

@Entity
@Table(name = "quotes")
public class QuoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long snowflake;

    @Column(nullable = false)
    private String quote;

    @Column
    private String quoteMaker;

    public QuoteEntity() {}

    public QuoteEntity(Long snowflake, String quote, String quoteMaker) {
        this.snowflake = snowflake;
        this.quote = quote;
        this.quoteMaker = quoteMaker;
    }

    public Long getId() {
        return id;
    }

    public Long getSnowflake() {
        return snowflake;
    }

    public void setSnowflake(Long snowflake) {
        this.snowflake = snowflake;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getQuoteMaker() {
        return quoteMaker;
    }

    public void setQuoteMaker(String quoteMaker) {
        this.quoteMaker = quoteMaker;
    }
}

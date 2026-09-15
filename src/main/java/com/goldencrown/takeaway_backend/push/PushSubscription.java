package com.goldencrown.takeaway_backend.push;

import jakarta.persistence.*;

@Entity
@Table(name = "push_subscriptions")
public class PushSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, unique = true)
    private String endpoint;

    private String p256dh;
    private String auth;

    protected PushSubscription() {}

    public PushSubscription(String endpoint, String p256dh, String auth) {
        this.endpoint = endpoint;
        this.p256dh = p256dh;
        this.auth = auth;
    }

    public Long getId() { return id; }
    public String getEndpoint() { return endpoint; }
    public String getP256dh() { return p256dh; }
    public String getAuth() { return auth; }
}

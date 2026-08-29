package com.goldencrown.takeaway_backend.order;

import java.util.List;

public record OrderLineRequest(Long menuItemId, int quantity, String note, List<String> extraNames) {}

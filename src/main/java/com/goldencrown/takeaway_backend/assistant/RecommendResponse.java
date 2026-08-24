package com.goldencrown.takeaway_backend.assistant;

import com.goldencrown.takeaway_backend.menu.MenuItem;

import java.util.List;

public record RecommendResponse(String reply, List<MenuItem> recommendedItems) {}

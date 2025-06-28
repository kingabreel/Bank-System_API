package com.gab.apibank_system.domain.response;

import java.util.List;

public record PixResponse (List<String> keys, String account) {
}

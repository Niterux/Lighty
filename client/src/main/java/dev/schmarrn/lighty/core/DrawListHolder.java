// Copyright 2022-2023 The Lighty contributors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package dev.schmarrn.lighty.core;

import com.mojang.blaze3d.platform.MemoryTracker;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.util.HashMap;
import java.util.Map;

public class DrawListHolder implements AutoCloseable {
    // List because we can hold multiple gpuBuffers from different data providers
    private final Object2IntArrayMap<String> overlayBuffers;
    private final Map<String, Boolean> isValid;

    DrawListHolder() {
        this.overlayBuffers = new Object2IntArrayMap<>();
        this.isValid = new HashMap<>();
    }

    boolean isValid(String key) {
        return this.isValid.getOrDefault(key, false);
    }

    @Override
    public void close() {
        // Release the draw list, unmapped.
        this.overlayBuffers.values().forEach(MemoryTracker::m_1450705);
    }

    void upload(int drawList, String dataProviderKey, boolean builtSomething) {
        if (!builtSomething) {
            this.isValid.put(dataProviderKey, false);
            return;
        }
        this.overlayBuffers.put(dataProviderKey, drawList);
        this.isValid.put(dataProviderKey, true);
    }

    Object2IntMap<String> getLists() {
        return this.overlayBuffers;
    }
}

/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.services.patch.impl;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.trebol.api.models.SellStatusPojo;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.services.patch.SellStatusesPatchService;

@Service
@NoArgsConstructor
public class SellStatusesPatchServiceImpl
  implements SellStatusesPatchService {

  @Override
  public SellStatus patchExistingEntity(SellStatusPojo changes, SellStatus existing) {
    SellStatus target = SellStatus.builder()
      .id(existing.getId())
      .code(existing.getCode())
      .name(existing.getName())
      .build();

    Integer code = changes.getCode();
    if (code != null && !target.getCode().equals(code)) {
      target.setCode(code);
    }

    String name = changes.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    return target;
  }
}

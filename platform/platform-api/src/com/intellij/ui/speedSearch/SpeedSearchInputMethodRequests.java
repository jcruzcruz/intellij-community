// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.ui.speedSearch;

import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodRequests;
import java.text.AttributedCharacterIterator;

abstract public class SpeedSearchInputMethodRequests implements InputMethodRequests {
  abstract protected InputMethodRequests getDelegate();
  protected void ensurePopupIsShown() {}

  @Override
  public Rectangle getTextLocation(TextHitInfo offset) {
    InputMethodRequests delegate = getDelegate();
    if (delegate == null) {
      return new Rectangle();
    } else {
      return delegate.getTextLocation(offset);
    }
  }

  @Nullable
  @Override
  public TextHitInfo getLocationOffset(int x, int y) {
    InputMethodRequests delegate = getDelegate();
    if (delegate == null) {
      return null;
    } else {
      return delegate.getLocationOffset(x, y);
    }
  }

  @Override
  public int getInsertPositionOffset() {
    InputMethodRequests delegate = getDelegate();
    if (delegate == null) {
      return 0;
    } else {
      return delegate.getInsertPositionOffset();
    }
  }

  @Override
  public AttributedCharacterIterator getCommittedText(int beginIndex, int endIndex, AttributedCharacterIterator.Attribute[] attributes) {
    ensurePopupIsShown();
    InputMethodRequests delegate = getDelegate();
    assert delegate != null;
    return delegate.getCommittedText(beginIndex, endIndex, attributes);
  }

  @Override
  public int getCommittedTextLength() {
    ensurePopupIsShown();
    InputMethodRequests delegate = getDelegate();
    assert delegate != null;
    return delegate.getCommittedTextLength();
  }

  @Nullable
  @Override
  public AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] attributes) {
    InputMethodRequests delegate = getDelegate();
    if (delegate == null) {
      return null;
    } else {
      return delegate.cancelLatestCommittedText(attributes);
    }
  }

  @Nullable
  @Override
  public AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] attributes) {
    InputMethodRequests delegate = getDelegate();
    if (delegate == null) {
      return null;
    } else {
      return delegate.getSelectedText(attributes);
    }
  }
}
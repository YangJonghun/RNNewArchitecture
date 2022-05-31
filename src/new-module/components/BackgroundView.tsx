import React, {
  forwardRef,
  useCallback,
  useImperativeHandle,
  useRef,
} from 'react';
import NativeBackgroundView, {Commands} from './BackgroundViewNativeComponent';
import type {
  NativeBackgroundViewType,
  NativeBackgroundViewProps,
  OnChangeBackground,
} from './BackgroundViewNativeComponent';

export type BackgroundViewRef = Partial<
  React.ElementRef<NativeBackgroundViewType>
> & {
  changeBackgroundColor(color: string): void;
};

export type BackgroundViewProps = Omit<
  NativeBackgroundViewProps,
  'onChangeBackground'
> & {
  onChangeBackground?(color: string): void;
};

const BackgroundView = forwardRef<
  BackgroundViewRef | null,
  BackgroundViewProps
>(({onChangeBackground, ...props}, ref) => {
  const nativeRef = useRef<React.ElementRef<NativeBackgroundViewType> | null>(
    null,
  );

  const changeBackgroundColor = useCallback((color: string) => {
    if (nativeRef.current) {
      Commands.setBackgroundColor(nativeRef.current, color); // run 'setBackgroundColor' command
    }
  }, []);

  const _onChangeBackground = useCallback<OnChangeBackground>(
    event => {
      onChangeBackground?.(event.nativeEvent.color);
    },
    [onChangeBackground],
  );

  useImperativeHandle(
    ref,
    () => ({...(nativeRef.current ?? {}), changeBackgroundColor}),
    [changeBackgroundColor],
  );

  return (
    <NativeBackgroundView
      ref={nativeRef}
      onChangeBackground={_onChangeBackground}
      {...props}
    />
  );
});

export default BackgroundView;

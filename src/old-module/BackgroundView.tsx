import React, {
  forwardRef,
  useCallback,
  useImperativeHandle,
  useRef,
} from 'react';
import {findNodeHandle, requireNativeComponent, UIManager} from 'react-native';
import type {HostComponent, NativeSyntheticEvent} from 'react-native';
import type {ViewProps} from 'react-native';

const VIEW_NAME = 'OldBackgroundView';
enum COMMANDS {
  setBackgroundColor = 'setBackgroundColor',
}

type OnChangeBackgroundEvent = NativeSyntheticEvent<{color: string}>;

interface RCTBackgroundViewProps extends ViewProps {
  color?: string;
  step?: number;
  onChangeBackground?(event: OnChangeBackgroundEvent): void;
}

type RCTBackgroundViewType = HostComponent<RCTBackgroundViewProps>;

const RCTBackgroundView =
  requireNativeComponent<RCTBackgroundViewProps>(VIEW_NAME);

export type BackgroundViewRef = Partial<
  React.ElementRef<RCTBackgroundViewType>
> & {
  changeBackgroundColor(color: string): void;
};

export type BackgroundViewProps = Omit<
  RCTBackgroundViewProps,
  'onChangeBackground'
> & {
  onChangeBackground?(color: string): void;
};

const BackgroundView = forwardRef<
  BackgroundViewRef | null,
  BackgroundViewProps
>(({onChangeBackground, ...props}, ref) => {
  const nativeRef = useRef<React.ElementRef<RCTBackgroundViewType> | null>(
    null,
  );

  const changeBackgroundColor = useCallback((color: string) => {
    const viewId = findNodeHandle(nativeRef.current);
    const command =
      UIManager.getViewManagerConfig(VIEW_NAME).Commands[
        COMMANDS.setBackgroundColor
      ].toString();

    UIManager.dispatchViewManagerCommand(
      viewId,
      command, // run 'setBackgroundColor' command
      [color], // arguments array
    );
  }, []);

  const _onChangeBackground = useCallback(
    (event: OnChangeBackgroundEvent) =>
      onChangeBackground?.(event.nativeEvent.color),
    [onChangeBackground],
  );

  useImperativeHandle(
    ref,
    () => ({...(nativeRef.current ?? {}), changeBackgroundColor}),
    [changeBackgroundColor],
  );

  return (
    <RCTBackgroundView
      ref={nativeRef}
      onChangeBackground={_onChangeBackground}
      {...props}
    />
  );
});

export default BackgroundView;

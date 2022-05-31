import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';
import codegenNativeCommands from 'react-native/Libraries/Utilities/codegenNativeCommands';
import {
  Int32,
  DirectEventHandler,
} from 'react-native/Libraries/Types/CodegenTypes';
import type {ViewProps, HostComponent} from 'react-native';

export type OnChangeBackground = DirectEventHandler<Readonly<{color: string}>>;

export interface NativeBackgroundViewProps extends ViewProps {
  color?: string;
  step?: Int32;
  onChangeBackground?: OnChangeBackground;
}

export type NativeBackgroundViewType = HostComponent<NativeBackgroundViewProps>;

interface NativeCommands {
  setBackgroundColor: (
    viewRef: React.ElementRef<NativeBackgroundViewType>,
    color: string,
  ) => void;
}

export const Commands: NativeCommands = codegenNativeCommands<NativeCommands>({
  supportedCommands: ['setBackgroundColor'],
});

export default codegenNativeComponent<NativeBackgroundViewProps>(
  'NewBackgroundView',
);

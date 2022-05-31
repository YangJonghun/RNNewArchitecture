import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  sendQuestion(input: string): number;
}

export const AnswerModule =
  TurboModuleRegistry.getEnforcing<Spec>('NewAnswerModule');

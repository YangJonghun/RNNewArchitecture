import {NativeModules} from 'react-native';

const MODULE_NAME = 'OldAnswerModule';

const NativeAnswerModule = NativeModules[MODULE_NAME];

export const AnswerModule = {
  sendQuestion(question: string): number | undefined {
    return NativeAnswerModule?.sendQuestion(question);
  },
};

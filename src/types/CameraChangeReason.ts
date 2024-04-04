/**
 * Developer: 개발자가 API를 호출해 카메라가 움직였음을 나타냅니다. 기본값입니다.
 * Gesture: 사용자의 제스처로 인해 카메라가 움직였음을 나타냅니다.
 * Control: 사용자의 버튼 선택으로 인해 카메라가 움직였음을 나타냅니다.
 * Location: 위치 트래킹 기능으로 인해 카메라가 움직였음을 나타냅니다.
 */
export type CameraChangeReason =
  | 'Developer'
  | 'Gesture'
  | 'Control'
  | 'Location';
export function cameraChangeReasonFromNumber(
  value: number
): CameraChangeReason {
  switch (value) {
    case 0:
      return 'Developer';
    case 1:
      return 'Gesture';
    case 2:
      return 'Control';
    case 3:
      return 'Location';
    default:
      return 'Developer';
  }
}
import Button from '@mui/material/Button';
import {type ReactElement} from 'react';
import {ReactAdapterElement, type RenderHooks} from "Frontend/generated/flow/ReactAdapter";

class MuiButtonElement extends ReactAdapterElement {
	protected override render(hooks: RenderHooks): ReactElement | null {
    const [label, setLabel] = hooks.useState<String>('label');

		return <Button variant="contained">{label}</Button>;
	}
}

customElements.define('mui-button', MuiButtonElement);
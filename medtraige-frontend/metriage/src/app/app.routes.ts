import { Routes } from '@angular/router';

export const routes: Routes = [
	{
		path: 'citas',
		loadComponent: () =>
			import('./citas/cita-list.component').then((component) => component.CitaListComponent)
	},
	{
		path: 'nueva-cita',
		loadComponent: () =>
			import('./citas/cita-form.component').then((component) => component.CitaFormComponent)
	},
	{ path: '', pathMatch: 'full', redirectTo: 'citas' },
	{ path: '**', redirectTo: 'citas' }
];
